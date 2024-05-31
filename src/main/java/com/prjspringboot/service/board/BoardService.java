package com.prjspringboot.service.board;

import com.prjspringboot.domain.board.Board;
import com.prjspringboot.domain.board.BoardFile;
import com.prjspringboot.mapper.board.BoardMapper;
import com.prjspringboot.mapper.comment.CommentMapper;
import com.prjspringboot.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper mapper;
    private final MemberMapper memberMapper;
    private final S3Client s3Client;
    private final CommentMapper commentMapper;

    @Value("${aws.s3.bucket.name}")
    String bucketName;

    @Value("${image.src.prefix}")
    String srcPrefix;


    public void add(Board board, Authentication authentication, MultipartFile[] files) throws IOException {

        board.setMemberId(Integer.valueOf(authentication.getName()));

        mapper.add(board);

        //실제 파일 저장
        if (files != null) {
            for (MultipartFile file : files) {
                //db에 해당 게시물의 파일 목록 저장
                mapper.insertFileName(board.getId(), file.getOriginalFilename());
                //실제 파일 저장(s3)
                String key = STR."prj2/\{board.getId()}/\{file.getOriginalFilename()}";
                PutObjectRequest objectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .build();
                s3Client.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            }
        }


    }

    public boolean validate(Board board) {

        if (board.getTitle() == null || board.getTitle().isBlank()) {
            return false;
        }
        if (board.getContent() == null || board.getContent().isBlank()) {
            return false;
        }
        return true;
    }

    public Map<String, Object> list(Integer page, String searchType, String keyword) {

        Map<String, Object> pageInfo = new HashMap();


        Integer countAll = mapper.countAllWithSearch(searchType, keyword);


        Integer offset = (page - 1) * 10;
        Integer lastPageNumber = (countAll - 1) / 10 + 1;
        Integer leftPageNumber = (page - 1) / 10 * 10 + 1;
        Integer rightPageNumber = leftPageNumber + 9;

        rightPageNumber = Math.min(rightPageNumber, lastPageNumber);
        leftPageNumber = rightPageNumber - 9;
        leftPageNumber = Math.max(leftPageNumber, 1);


        pageInfo.put("currentPageNumber", page);
        pageInfo.put("lastPageNumber", lastPageNumber);
        pageInfo.put("leftPageNumber", leftPageNumber);
        pageInfo.put("rightPageNumber", rightPageNumber);

        Integer prevPageNumber = leftPageNumber - 1;
        Integer nextPageNumber = rightPageNumber + 1;

        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if (nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }


        return Map.of("boardList", mapper.selectAllPaging(offset, searchType, keyword),
                "pageInfo", pageInfo);
    }

    public Map<String, Object> get(Integer id, Authentication authentication) {
        Map<String, Object> result = new HashMap<>();

        Board board = mapper.selectById(id);
        List<String> fileNames = mapper.selectFileNameByBoardId(id);
        // 버킷객체URL/{id}/{name}
        List<BoardFile> files = fileNames.stream()
                .map(name -> new BoardFile(name, STR."\{srcPrefix}\{id}/\{name}"))
                .toList();

        board.setFileList(files);

        Map<String, Object> like = new HashMap<>();
        if (authentication == null) {
            like.put("like", false);
        } else {
            int c = mapper.selectLikeByBoardIdAndMemberId(id, authentication.getName());
            like.put("like", c == 1);
        }
        like.put("count", mapper.selectCountLikeByBoardId(id));
        result.put("board", board);
        result.put("like", like);

        return result;
    }

    public void remove(Integer id) {

        //file명 조회
        List<String> fileNames = mapper.selectFileNameByBoardId(id);


        //s3에 있는 file
        for (String fileName : fileNames) {
            String key = STR."prj2/\{id}/\{fileName}";
            DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(objectRequest);
        }


        //댓글 삭제
        commentMapper.deleteByBoardId(id);

        //board_file 지우기
        mapper.deleteFileByBoardId(id);

        //board_like
        mapper.deleteLikeBoardId(id);

        //board
        mapper.deleteById(id);
    }

    public void edit(Board board, List<String> removeFileList, MultipartFile[] addFileList) throws IOException {

        if (removeFileList != null && removeFileList.size() > 0) {
            //disk의 파일 삭제
            for (String fileName : removeFileList) {

                String key = STR."prj2/\{board.getId()}/\{fileName}";
                DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();
                s3Client.deleteObject(objectRequest);


                //db records삭제
                mapper.deleteFileByBoardIdAndName(board.getId(), fileName);
            }
        }

        if (addFileList != null && addFileList.length > 0) {
            List<String> fileNameList = mapper.selectFileNameByBoardId(board.getId());
            for (MultipartFile file : addFileList) {
                String fileName = file.getOriginalFilename();
                if (!fileNameList.contains(fileName)) {
                    // 새 파일이 기존에 없을 때만 db에 추가
                    mapper.insertFileName(board.getId(), fileName);
                }
                // s3에 쓰기
                String key = STR."prj2/\{board.getId()}/\{fileName}";
                PutObjectRequest objectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .build();

                s3Client.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            }
        }
        mapper.update(board);
    }

    public boolean hasAccess(Integer id, Authentication authentication) {
        Board board = mapper.selectById(id);

        return board.getMemberId().equals(Integer.valueOf(authentication.getName()));
    }

    public Map<String, Object> like(Map<String, Object> req, Authentication authentication) {

        Map<String, Object> result = new HashMap<>();

        result.put("like", false);
        Integer boardId = (Integer) req.get("boardId");
        Integer memberId = Integer.valueOf(authentication.getName());

        //이미 했으면
        int count = mapper.deleteLikeByBoardIdAndMemberId(boardId, memberId);

        //안했으면
        if (count == 0) {
            mapper.insertLikeByBoardIdAndMemberId(boardId, memberId);
            result.put("like", true);
        }
        result.put("count", mapper.selectCountLikeByBoardId(boardId));

        return result;
    }

}

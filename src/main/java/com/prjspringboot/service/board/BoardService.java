package com.prjspringboot.service.board;

import com.prjspringboot.domain.board.Board;
import com.prjspringboot.domain.board.BoardFile;
import com.prjspringboot.mapper.board.BoardMapper;
import com.prjspringboot.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.File;
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


    public void add(Board board, Authentication authentication, MultipartFile[] files) throws IOException {

        board.setMemberId(Integer.valueOf(authentication.getName()));

        mapper.add(board);

        //실제 파일 저장
        if (files != null) {
            for (MultipartFile file : files) {
                //db에 해당 게시물의 파일 목록 저장
                mapper.insertFileName(board.getId(), file.getOriginalFilename());
                //실제 파일 저장
                //부모 디렉토리 만들기
                String dir = STR."/Users/igyeyeong/Desktop/Temp/prj-reactspring/\{board.getId()}";
                File dirFile = new File(dir);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }

                //파일 경로
                String path = STR."/Users/igyeyeong/Desktop/Temp/prj-reactspring/\{board.getId()}/\{file.getOriginalFilename()}";
                File destination = new File(path);
                file.transferTo(destination);
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

    public Board get(Integer id) {

        Board board = mapper.selectById(id);
        List<String> fileNames = mapper.selectFileNameByBoardId(id);
        List<BoardFile> files = fileNames.stream()
                .map(name -> new BoardFile(name, STR."http://127.0.0.1:8888/\{id}/\{name}"))
                .toList();

        board.setFileList(files);
        return board;
    }

    public void remove(Integer id) {

        //file명 조회
        List<String> fileNames = mapper.selectFileNameByBoardId(id);


        //실제 파일 지우기
        String dir = STR."/Users/igyeyeong/Desktop/Temp/prj-reactspring/\{id}/";
        for (String fileName : fileNames) {
            File file = new File(dir + fileName);
            file.delete();
        }
        File dirFile = new File(dir);
        if (dirFile.exists()) {
            dirFile.delete();
        }

        //board_file 지우기
        mapper.deleteFileByBoardId(id);

        //board
        mapper.deleteById(id);
    }

    public void edit(Board board, List<String> removeFileList, MultipartFile[] addFileList) throws IOException {

        if (removeFileList != null && removeFileList.size() > 0) {
            //disk의 파일 삭제
            for (String fileName : removeFileList) {

                String path = STR."/Users/igyeyeong/Desktop/Temp/prj-reactspring/\{board.getId()}/\{fileName}";
                File file = new File(path);
                file.delete();

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
                // disk 에 쓰기
                File dir = new File(STR."/Users/igyeyeong/Desktop/Temp/prj-reactspring/\{board.getId()}");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String path = STR."/Users/igyeyeong/Desktop/Temp/prj-reactspring/\{board.getId()}/\{fileName}";
                File destination = new File(path);
                file.transferTo(destination);
            }
        }
        mapper.update(board);
    }

    public boolean hasAccess(Integer id, Authentication authentication) {
        Board board = mapper.selectById(id);

        return board.getMemberId().equals(Integer.valueOf(authentication.getName()));
    }
}

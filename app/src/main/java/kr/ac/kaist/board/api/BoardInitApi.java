package kr.ac.kaist.board.api;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

import kr.ac.kaist.board.model.Board;
import kr.ac.kaist.board.model.Wall;
import kr.ac.kaist.board.utils.Argument;

/**
 * Created by yearnning on 2014. 8. 9..
 */
public class BoardInitApi extends ApiBase {

    private static final String TAG = "BoardInitApi";
    private ArrayList<Board> boards = null;

    /**
     */
    public BoardInitApi() {
        initBoards();
    }

    public ArrayList<Board> getResult() {
        return boards;
    }


    private void initBoards() {

        ArrayList<Board> boards = new ArrayList<>();

        //
        /*
        boards.add(
                createBoard(Argument.ARG_BOARD_BF, "대나무숲(bf.kaist.ac.kr)", "")
                        .addWall(createWall("대나무숲", "/posts?board=1", Argument.ARG_BOARD_BF))
        ); */

        //
        boards.add(
                createBoard(Argument.ARG_BOARD_ARA, "아라(ara.kaist.ac.kr)", "")
                        .addWall(createWall("뉴스피드", "/m/all/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("KAIST통합공지", "/m/board/Notice/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("Garbages", "/m/board/Garbages/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("Food", "/m/board/Food/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("Love", "/m/board/Love/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("Infoworld", "/m/board/Infoworld/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("FunLife", "/m/board/FunLife/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("Lostfound", "/m/board/Lostfound/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("Wanted", "/m/board/Wanted/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("BuySell", "/m/board/BuySell/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("QandA", "/m/board/QandA/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("취업정보", "/m/board/Jobs/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("취미전반", "/m/board/Hobby/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("Siggame", "/m/board/Siggame/", Argument.ARG_BOARD_ARA))
                        .addWall(createWall("ToSysop", "/m/board/ToSysop/", Argument.ARG_BOARD_ARA))
        );

        //
        boards.add(
                createBoard(Argument.ARG_BOARD_PORTAL_NOTICE, "KAIST포탈 - 공지사항", "")
                        .addWall(createWall("뉴스피드", "/api/notice/today_notice/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("학생공지", "/api/notice/student_notice/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("업무공지", "/api/notice/notice/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("수강/학적/논문", "/api/notice/lecture_academic_paper/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("교과과정", "/api/notice/academic_courses/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("리더십/인턴/상담", "/api/notice/leadership_intern_counseling/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("취업", "/api/notice/recruiting/", Argument.ARG_BOARD_PORTAL_NOTICE))
                                //.addWall(createWall("세미나/행사", "/api/notice/seminar_events"))
                        .addWall(createWall("생활관/장학/복지", "/api/notice/dormitory_scholarship_welfare/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("체육/건강관리/클리닉", "/api/notice/influenza_a_h1n1/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("컴퓨터/네트워크", "/api/notice/computer_network/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("IT서비스 뉴스레터", "/api/notice/it_newsletter/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("작업공지", "/api/notice/work_notice/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("원총/총학공지", "/api/notice/gsc_usc_notice/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("학생동아리소개", "/api/notice/student_club/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("연구개발사업", "/api/notice/rnd_notices/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("전문연구요원", "/api/notice/researcher_on_military_duty/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("International Community", "/api/notice/International/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("직원 경조사", "/api/notice/classified/", Argument.ARG_BOARD_PORTAL_NOTICE))
                        .addWall(createWall("직원 업무편람/양식", "/api/notice/manuals_forms/", Argument.ARG_BOARD_PORTAL_NOTICE))
        );

        //
        boards.add(
                createBoard(Argument.ARG_BOARD_PORTAL_BOARD, "KAIST포탈 - 게시판", "")
                        .addWall(createWall("뉴스피드", "/api/board/today_board/", Argument.ARG_BOARD_PORTAL_BOARD))
                        .addWall(createWall("자유게시판", "/api/board/discussions/", Argument.ARG_BOARD_PORTAL_BOARD))
                        .addWall(createWall("칭찬게시판", "/api/board/praises/", Argument.ARG_BOARD_PORTAL_BOARD))
                        .addWall(createWall("유실물게시판", "/api/board/lostfound/", Argument.ARG_BOARD_PORTAL_BOARD))
                        .addWall(createWall("제안상황판", "/api/board/general_proposals/", Argument.ARG_BOARD_PORTAL_BOARD))
                        .addWall(createWall("궁동아파트", "/api/board/gungdong_apt/", Argument.ARG_BOARD_PORTAL_BOARD))
                        .addWall(createWall("해외연수결과보고", "/api/board/overseas_staff_training_report/", Argument.ARG_BOARD_PORTAL_BOARD))
        );


        this.boards = boards;
    }

    private Board createBoard(int id, String title, String explain) {
        Board board = new Board();
        board.setId(id);
        board.setTitle(title);
        board.setExplain(explain);
        return board;
    }

    private Wall createWall(String title, String slug, int board_id) {
        Wall wall = new Wall();
        wall.setTitle(title);
        wall.setSlug(slug);
        wall.setBoard_id(board_id);
        return wall;
    }

}

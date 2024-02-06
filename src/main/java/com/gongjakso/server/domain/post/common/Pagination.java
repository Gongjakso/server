package com.gongjakso.server.domain.post.common;

public class Pagination {

    private int totalPostCount; // 전체 데이터 수
    private int totalPageCount; // 전체 페이지 수
    private int startPage; // 첫 페이지 번호
    private int endPage; // 끝 페이지 번호
    private int limitStart; // LIMIT 시작 위치
    private boolean existPrevPage; // 이전 페이지 존재여부
    private boolean existNextPage; // 다음 페이지 존재여부

    public Pagination(int totalRecordCount, int page, int size, int recordSize) {
        if (totalRecordCount > 0) {
            this.totalPostCount = totalRecordCount;
            calculation(page, 6);
        }
    }

    private void calculation(int page, int size) {
        // 전체 페이지 수 계산
        totalPageCount = ((totalPostCount - 1) / size) + 1;

        // 현재 페이지가 전체 페이지 수보다 큰 경우, 현재 페이지를 전체 페이지 수로 설정
        if (page > totalPageCount) {
            page = totalPageCount;
        }

        // 현재 페이지의 첫 post 번호 계산
        startPage = ((page - 1) / size) * size + 1;

        // 현재 페이지의  끝 post 번호 계산
        endPage = startPage + size - 1;

        // 끝 페이지가 전체 페이지 수보다 큰 경우, 끝 페이지를 전체 페이지 수로 설정
        if (endPage > totalPageCount) {
            endPage = totalPageCount;
        }

        // LIMIT 시작 위치 계산
        limitStart = (page - 1) * size;

        // 이전 페이지 존재 여부 확인
        existPrevPage = startPage != 1;

        // 다음 페이지 존재 여부 확인
        existNextPage = (endPage * size) < totalPostCount;
    }

    // 필요에 따라 getter 메서드 추가
    // 예: public int getTotalPageCount() { return totalPageCount; }
}

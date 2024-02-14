package com.gongjakso.server.domain.post.common;

public class Pagination {

    private int totalPostCount; // 전체 데이터 수
    private int totalPageCount; // 전체 페이지 수
    private int startPost; // 첫 페이지 번호
    private int endPost; // 끝 페이지 번호
    private int page;
    private boolean existPrevPage; // 이전 페이지 존재여부
    private boolean existNextPage; // 다음 페이지 존재여부

    public Pagination(int totalPostCount, int page, int size) {
        if (totalPostCount > 0) {
            this.totalPostCount = totalPostCount;
            this.page = page;
            calculation(page, size);
        }
    }

    private void calculation(int page, int size) {
        // 전체 페이지 수 계산
        totalPageCount = (totalPostCount + size - 1) / size;

        // 현재 페이지가 전체 페이지 수보다 큰 경우, 현재 페이지를 전체 페이지 수로 설정
        this.page = (page > totalPageCount) ? totalPageCount : page;

        // 현재 페이지의 첫 post 번호 계산
        startPost = (this.page - 1) * size + 1;

        // 현재 페이지의 끝 post 번호 계산
        endPost = startPost + size - 1;

        // 끝 페이지가 전체 페이지 수보다 큰 경우, 끝 페이지를 전체 페이지 수로 설정
        if (endPost > totalPostCount) {
            endPost = totalPostCount;
        }

        // 이전 페이지 존재 여부 확인
        existPrevPage = this.page > 0;

        // 다음 페이지 존재 여부 확인
        existNextPage = endPost < totalPostCount;
    }

    public int getPage() {
        return page - 1;
    }

    // 필요에 따라 getter 메서드 추가
    // 예: public int getTotalPageCount() { return totalPageCount; }
}

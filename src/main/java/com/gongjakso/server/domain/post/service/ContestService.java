package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.apply.dto.ApplyList;
import com.gongjakso.server.domain.apply.dto.CategoryRes;
import com.gongjakso.server.domain.apply.dto.PageRes;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.post.dto.ContestList;
import com.gongjakso.server.domain.post.dto.ContestPageRes;
import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.repository.CategoryRepository;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ContestService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    public ContestPageRes contestListPage(int page, int size, String region,String searchWord, String sort){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,sort));
        //지역,검색 조건 모두 없을 경우
        if(region.equals("null")&&searchWord.equals("null")){

        }
        //지역 조건만 있을 경우
        else if (!region.equals("null")&&searchWord.equals("null")) {

        }
        //검색 조건만 있을 경우
        else if (region.equals("null")&&!searchWord.equals("null")) {

        }
        //지역,검색 조건 모두 있을 경우
        else {

        }
        Page<Post> contestPage = postRepository.findAllByPostTypeAndEndDateAfter(false,LocalDateTime.now(),pageable);
        List<ContestList> contestLists = contestPage.getContent().stream()
                .map(post -> ContestList.of(post, calculateDate(post),createCategoryList(post)))
                .collect(Collectors.toList());
        int pageNo = contestPage.getNumber();
        int totalPages = contestPage.getTotalPages();
        boolean last = contestPage.isLast();
        return ContestPageRes.of(contestLists,pageNo,size,totalPages,last);
    }

    private int calculateDate(Post post){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = post.getEndDate();
        return (int) ChronoUnit.DAYS.between(now.toLocalDate(), endDate.toLocalDate());
    }

    private List<String> createCategoryList(Post post){
        List<Category> categoryList = categoryRepository.findCategoryByPost(post);
        List<String> list = new ArrayList<>();
        if(categoryList!=null){
            for (Category category : categoryList){
                for(int i=0;i<category.getSize();i++){
                    list.add(String.valueOf(category.getCategoryType()));
                }
            }
            return list;
        }else {
            throw new ApplicationException(ErrorCode.NOT_FOUND_CATEGORY_EXCEPTION);
        }
    }
}

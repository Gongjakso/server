package com.gongjakso.server.domain.post.controller;

import com.gongjakso.server.domain.post.dto.GetProjectRes;
import com.gongjakso.server.domain.post.dto.PostDeleteRes;
import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.dto.PostRes;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.domain.post.service.PostService;
import com.gongjakso.server.global.common.ApplicationResponse;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Tag(name = "Post", description = "공고 관련 API")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    @PostMapping("")
    public ApplicationResponse<PostRes> create(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody PostReq req) {
        return ApplicationResponse.ok(postService.create(principalDetails.getMember(), req));
    }
    @PutMapping("/{id}")
    public ApplicationResponse<PostRes> modify(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long id, @RequestBody PostReq req) {
        return ApplicationResponse.ok(postService.modify(principalDetails.getMember(), id, req));
    }

    @PatchMapping("/{id}")
    public ApplicationResponse<PostDeleteRes> delete(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long id){
        return ApplicationResponse.ok(postService.delete(principalDetails.getMember(), id));
    }

    @GetMapping("/project")
    public ApplicationResponse<Page<GetProjectRes>> list(@PageableDefault(size = 6) Pageable pageable,
                                                         @RequestParam(value = "searchWord", required = false) String searchWord) {
        try {
            if (!searchWord.isBlank()) {
                return ApplicationResponse.ok(postService.getProjectsBySearchWord(searchWord, pageable));
                /*
                if (stackName.isBlank() && location.isBlank()) {
                    return new ApplicationResponse<>(postService.getProjectsBySearchWord(searchWord, pageable));
                } else if (!stackName.isBlank() && location.isBlank()) {
                    return new ApplicationResponse<>(postService.getProjectsByStackNameAndSearchWord(stackName, searchWord, pageable));
                } else if (stackName.isBlank() && !location.isBlank()) {
                    return new ApplicationResponse<>(postService.getProjectsByLocationAndSearchWord(location,searchWord, pageable));
                } else {
                    return new ApplicationResponse<>(postService.getProjectsByLocationAndStackNameAndSearchWord(location, stackName, searchWord, pageable));
                }
                 */
            } else {
                return ApplicationResponse.ok(postService.getProjectsBySearchWord(searchWord, pageable));
                /*
                if (stackName.isBlank() && location.isBlank()) {
                    return ApplicationResponse.ok(postService.getProjects(pageable));
                } else if (!stackName.isBlank() && location.isBlank()) {
                    return new ApplicationResponse<>(postService.getProjectsByStackName(stackName, pageable));
                } else if (stackName.isBlank() && !location.isBlank()) {
                    return new ApplicationResponse<>(postService.getProjectsByLocation(location, pageable));
                } else {
                    return new ApplicationResponse<>(postService.getProjectsLocationAndStackName(location, stackName, pageable));
                }
                 */
            }
        } catch (Exception e) {
            return ApplicationResponse.ok(postService.getProjectsBySearchWord(searchWord, pageable));
        }
    }
}
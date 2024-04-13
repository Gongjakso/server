package com.gongjakso.server.domain.apply.entity;

import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "applyStack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplyStack{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applyStack_id",nullable = false,columnDefinition = "bigint")
    private Long applyStackId;

    @ManyToOne
    @JoinColumn(name = "apply_id")
    private Apply apply;

    @ManyToOne
    @JoinColumn(name = "stack_id")
    private StackName stackName;

    @Builder
    public ApplyStack(Long applyStackId,Apply apply,StackName stackName){
        this.applyStackId=applyStackId;
        this.apply=apply;
        this.stackName=stackName;
    }
}

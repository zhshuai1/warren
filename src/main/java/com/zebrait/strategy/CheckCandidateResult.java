package com.zebrait.strategy;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CheckCandidateResult {
    private boolean qualified;
}

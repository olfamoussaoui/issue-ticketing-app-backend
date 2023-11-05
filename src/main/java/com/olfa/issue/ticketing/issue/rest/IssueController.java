package com.olfa.issue.ticketing.issue.rest;

import com.olfa.issue.ticketing.issue.service.IssueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/issues")
public class IssueController {
    private final IssueService issueService;
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }
}

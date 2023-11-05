package com.olfa.issue.ticketing.issue.configuration;

import com.olfa.issue.ticketing.issue.mapper.IssueDTOMapper;
import com.olfa.issue.ticketing.issue.repositories.IssueRepository;
import com.olfa.issue.ticketing.issue.repositories.IssueRepositoryImpl;
import com.olfa.issue.ticketing.issue.repositories.IssueRepositoryJPA;
import com.olfa.issue.ticketing.issue.service.IssueService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IssueConfiguration {
    @Bean
    public IssueRepository issueRepository(final IssueRepositoryJPA issueRepositoryJPA) {
        return new IssueRepositoryImpl(issueRepositoryJPA);
    }

    @Bean
    public IssueDTOMapper issueDTOMapper() {
        return new IssueDTOMapper();
    }

    @Bean
    public IssueService issueService(final IssueRepository issueRepository, final IssueDTOMapper issueDTOMapper) {
        return new IssueService(issueRepository, issueDTOMapper);
    }
}

package com.olfa.issue.ticketing.issue.configuration;

import com.olfa.issue.ticketing.issue.mapper.IssueResponseDTOMapper;
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
    public IssueResponseDTOMapper issueResponseDTOMapper() {
        return new IssueResponseDTOMapper();
    }

    @Bean
    public IssueService issueService(final IssueRepository issueRepository, final IssueResponseDTOMapper issueResponseDTOMapper) {
        return new IssueService(issueRepository, issueResponseDTOMapper);
    }
}

package Jira;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.*;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static j2html.TagCreator.*;

public class JiraClient {

    String username;
    String password;
    String jiraUrl;
    JiraRestClient restClient;


    public JiraClient(String username, String password, String jiraUrl) {
        this.username = username;
        this.password = password;
        this.jiraUrl = jiraUrl;
        this.restClient = getJiraRestClient();
    }

    public static void main(String[] args) throws IOException {

        final String JIRA_URL = "https://curatio.atlassian.net/";
        final String JIRA_ADMIN_USERNAME = "bruno@curatio.me";
        final String JIRA_ADMIN_PASSWORD = "bacelar.1507";

        JiraClient JiraClient = new JiraClient(JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD, JIRA_URL);

        // Invoke the JRJC Client
//        Promise<User> promise = JiraClient.restClient.getUserClient().getUser("bruno@curatio.me");
//        User user = promise.claim();

        for (BasicProject project : JiraClient.restClient.getProjectClient().getAllProjects().claim()) {
            System.out.println(project.getKey() + ": " + project.getName());
        }

        Promise<SearchResult> searchJqlPromise = JiraClient.restClient.getSearchClient().searchJql("project = MB and component in (Android, iOS) AND labels in (Reviewed, Reviewed2, Reviewed3, Reviewed4) AND updated >= 2018-09-01 AND updated <= 2018-09-19 AND assignee in (mehrdad, mohammad.feyzian, mostafa) ORDER BY created DESC");

        for (Issue issue : searchJqlPromise.claim().getIssues()) {
            System.out.println("Summary: " + issue.getKey() + " - " + issue.getSummary());
            System.out.println("");
            System.out.println("Labels: " + issue.getLabels());
            System.out.println(" ");
            System.out.println("Assignee: " + issue.getAssignee().getDisplayName());
            System.out.println(" ");
            System.out.println("Status: " + issue.getStatus().getName());
            System.out.println("--------------------------");
        }

        // Print the result
//        System.out.println(String.format("Your admin user's email address is: %s\r\n", user.getEmailAddress()));
        generateHTMLFile();
        // Done
        System.out.println("Example complete. Now exiting.");


        System.exit(0);

//        final String issueKey = JiraClient.createIssue("PT", 1003L, "Issue created from JRJC");
//        JiraClient.updateIssueDescription(issueKey, "This is description from my Jira Client");
//        Issue issue = JiraClient.getIssue(issueKey);
//        System.out.println(issue.getDescription());
//
//        JiraClient.voteForAnIssue(issue);
//
//        System.out.println(JiraClient.getTotalVotesCount(issueKey));
//
//        JiraClient.addComment(issue, "This is comment from my Jira Client");
//
//        List<Comment> comments = JiraClient.getAllComments(issueKey);
//        comments.forEach(c -> System.out.println(c.getBody()));
//
//        JiraClient.deleteIssue(issueKey, true);
//
//        JiraClient.restClient.close();
    }

    private String createIssue(String projectKey, Long issueType, String issueSummary) {

        IssueRestClient issueClient = restClient.getIssueClient();

        IssueInput newIssue = new IssueInputBuilder(projectKey, issueType, issueSummary).build();

        return issueClient.createIssue(newIssue).claim().getKey();
    }

    private Issue getIssue(String issueKey) {
        return restClient.getIssueClient().getIssue(issueKey).claim();
    }

    private void voteForAnIssue(Issue issue) {
        restClient.getIssueClient().vote(issue.getVotesUri()).claim();
    }

    private int getTotalVotesCount(String issueKey) {
        BasicVotes votes = getIssue(issueKey).getVotes();
        return votes == null ? 0 : votes.getVotes();
    }

    private void addComment(Issue issue, String commentBody) {
        restClient.getIssueClient().addComment(issue.getCommentsUri(), Comment.valueOf(commentBody));
    }

    private List<Comment> getAllComments(String issueKey) {
        return StreamSupport.stream(getIssue(issueKey).getComments().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void updateIssueDescription(String issueKey, String newDescription) {
        IssueInput input = new IssueInputBuilder().setDescription(newDescription).build();
        restClient.getIssueClient().updateIssue(issueKey, input).claim();
    }

    private void deleteIssue(String issueKey, boolean deleteSubtasks) {
        restClient.getIssueClient().deleteIssue(issueKey, deleteSubtasks).claim();
    }

    private JiraRestClient getJiraRestClient() {
        return new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(getJiraUri(), this.username, this.password);
    }

    private void readAllIssues(){
        // Invoke the JRJC Client
        Promise<User> promise = restClient.getUserClient().getUser("bruno@curatio.me");
        User user = promise.claim();

        for (BasicProject project : restClient.getProjectClient().getAllProjects().claim()) {
            System.out.println(project.getKey() + ": " + project.getName());
        }

        Promise<SearchResult> searchJqlPromise = restClient.getSearchClient().searchJql("component in (Android, iOS) AND labels in (Reviewed, Reviewed2, Reviewed3, Reviewed4) AND updated >= 2018-09-01 AND updated <= 2018-09-19 AND assignee in (mehrdad, mohammad.feyzian, mostafa) ORDER BY created DESC");

        for (Issue issue : searchJqlPromise.claim().getIssues()) {
            System.out.println(issue.getSummary());
        }

        // Print the result
        System.out.println(String.format("Your admin user's email address is: %s\r\n", user.getEmailAddress()));

        // Done
        System.out.println("Example complete. Now exiting.");
        System.exit(0);
    }


    public static void generateHTMLFile(){
        body(
                h1("Hello, World!"),
                img().withSrc("http://new.curatio.me/wp-content/uploads/2017/02/signin-logo-e1487192618816.png")
        ).render();
    }


    private URI getJiraUri() {
        return URI.create(this.jiraUrl);
    }
}


package Jira;

import Jira.Log.Test;
import Jira.Log.Testrun;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.lang.ArrayUtils.isEquals;

public class Utils {

    static JiraClient jiraClient;
    private static JiraRestClient restClient;

    public static void main(String args[]) {

        try {

            connectJira();
            readXMLFile();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void connectJira() {

        jiraClient = new JiraClient(
                "bruno@curatio.me",
                "bacelar.1507",
                "https://curatio.atlassian.net/");

        restClient = jiraClient.restClient;

    }

    public static String createIssue(String projectKey, Long issueType, String issueSummary) {

        IssueRestClient issueClient = restClient.getIssueClient();
        IssueInput newIssue = new IssueInputBuilder(
                projectKey, issueType, issueSummary).build();

        return issueClient.createIssue(newIssue).claim().getKey();
    }


    public static void readXMLFile() {

        String xmlPathFile = "/Users/curatio/Documents/QA_TestCases/logs/Test Results_log.xml";

        readOutPutXMLFile(xmlPathFile);
    }

    public static String readOutPutXMLFile(String pathOfXmlFile) {


        String result = null;
        try (InputStream xml = new BufferedInputStream(
                Files.newInputStream(Paths.get(pathOfXmlFile)))) {

            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList outputNodes = (NodeList) xpath.evaluate("//test/output",
                    new InputSource(xml), XPathConstants.NODESET);

            int count = outputNodes.getLength();
            for (int i = 0; i < count; i++) {
                Node outputNode = outputNodes.item(i);
                System.out.println(
                        "output node content: " + i + " ====>" + outputNode.getTextContent());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return result;
    }
}

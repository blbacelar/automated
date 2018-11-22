package Jira;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IssueType {

    @SerializedName("questionId")
    @Expose
    Integer questionId;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("difficulty")
    @Expose
    Integer difficulty;

}

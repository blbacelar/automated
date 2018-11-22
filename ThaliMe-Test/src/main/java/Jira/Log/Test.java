package Jira.Log;

public class Test {

    private String duration;
    private String locationUrl;
    private String status;
    private String name;
    private OutputLog[] output;

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    public String getLocationUrl ()
    {
        return locationUrl;
    }

    public void setLocationUrl (String locationUrl)
    {
        this.locationUrl = locationUrl;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public OutputLog[] getOutput ()
    {
        return output;
    }

    public void setOutput (OutputLog[] output)
    {
        this.output = output;
    }



    @Override
    public String toString()
    {
        return "[duration = "+duration+", locationUrl = "+locationUrl+", status = "+status+", name = "+name+", output = "+output+"]";
    }
}

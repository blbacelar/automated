package Jira.Log;

public class Root
{
    private String location;

    private String name;

    private OutputLog[] output;

    public String getLocation ()
    {
        return location;
    }

    public void setLocation (String location)
    {
        this.location = location;
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
        return "[location = "+location+", name = "+name+", output = "+output+"]";
    }
}

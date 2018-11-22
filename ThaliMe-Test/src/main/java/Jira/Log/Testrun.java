package Jira.Log;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;


public class Testrun {
    private String duration;

    private String footerText;

    private Count[] count;

    private Root root;

    @JacksonXmlElementWrapper(useWrapping=false, localName = "test")
    private Test[] test;

    private String name;

    private Config config;

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    public String getFooterText ()
    {
        return footerText;
    }

    public void setFooterText (String footerText)
    {
        this.footerText = footerText;
    }

    public Count[] getCount ()
    {
        return count;
    }

    public void setCount (Count[] count)
    {
        this.count = count;
    }

    public Root getRoot ()
    {
        return root;
    }

    public void setRoot (Root root)
    {
        this.root = root;
    }

    public Test[] getTest ()
    {
        return test;
    }

    public void setTest (Test[] test)
    {
        this.test = test;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Config getConfig ()
    {
        return config;
    }

    public void setConfig (Config config)
    {
        this.config = config;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [duration = "+duration+", footerText = "+footerText+", count = "+count+", root = "+root+", test = "+test+", name = "+name+", config = "+config+"]";
    }
}

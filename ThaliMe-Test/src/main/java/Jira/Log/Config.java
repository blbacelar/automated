package Jira.Log;

public class Config {
    private Module module;

    private String name;

    private String nameIsGenerated;

    private Option[] option;

    private String configId;

    public Module getModule ()
    {
        return module;
    }

    public void setModule (Module module)
    {
        this.module = module;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getNameIsGenerated ()
    {
        return nameIsGenerated;
    }

    public void setNameIsGenerated (String nameIsGenerated)
    {
        this.nameIsGenerated = nameIsGenerated;
    }

    public Option[] getOption ()
    {
        return option;
    }

    public void setOption (Option[] option)
    {
        this.option = option;
    }

    public String getConfigId ()
    {
        return configId;
    }

    public void setConfigId (String configId)
    {
        this.configId = configId;
    }

    @Override
    public String toString()
    {
        return "[module = "+module+", name = "+name+", nameIsGenerated = "+nameIsGenerated+", option = "+option+", configId = "+configId+"]";
    }
}

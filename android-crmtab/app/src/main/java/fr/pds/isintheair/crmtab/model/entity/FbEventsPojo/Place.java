package fr.pds.isintheair.crmtab.model.entity.FbEventsPojo;

import java.io.Serializable;

/**
 * Created by Muthu on 07/04/2016.
 */
public class Place implements Serializable
{
    private String id;

    private Location location;

    private String name;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Location getLocation ()
    {
        return location;
    }

    public void setLocation (Location location)
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

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", location = "+location+", name = "+name+"]";
    }
}
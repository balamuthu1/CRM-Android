package fr.pds.isintheair.crmtab.model.entity.FbEventsPojo;

import java.io.Serializable;

/**
 * Created by Muthu on 07/04/2016.
 */
public class Cursors implements Serializable
{
    private String after;

    private String before;

    public String getAfter ()
    {
        return after;
    }

    public void setAfter (String after)
    {
        this.after = after;
    }

    public String getBefore ()
    {
        return before;
    }

    public void setBefore (String before)
    {
        this.before = before;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [after = "+after+", before = "+before+"]";
    }
}

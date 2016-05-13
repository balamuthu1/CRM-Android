package fr.pds.isintheair.crmtab.model.entity.FbEventsPojo;

import java.io.Serializable;

/**
 * Created by Muthu on 07/04/2016.
 */
public class Paging implements Serializable
{
    private Cursors cursors;

    public Cursors getCursors ()
    {
        return cursors;
    }

    public void setCursors (Cursors cursors)
    {
        this.cursors = cursors;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [cursors = "+cursors+"]";
    }
}

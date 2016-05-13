package fr.pds.isintheair.crmtab.model.entity.FbEventsPojo;

import java.io.Serializable;

/**
 * Created by Muthu on 07/04/2016.
 */
public class FbEvent implements Serializable
{
    private Data[] data;

    private Paging paging;

    public Data[] getData ()
    {
        return data;
    }

    public void setData (Data[] data)
    {
        this.data = data;
    }

    public Paging getPaging ()
    {
        return paging;
    }

    public void setPaging (Paging paging)
    {
        this.paging = paging;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", paging = "+paging+"]";
    }
}
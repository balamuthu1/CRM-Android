package fr.pds.isintheair.crmtab.model.entity;

import java.util.List;

/**
 * Created by Muthu on 30/12/2015.
 */
public class ProductContainer {
    List<Product> list;


    public void setList(List<Product> list) {
        this.list = list;
    }

    public List<Product> getProducts(){
        return list;
    }

}

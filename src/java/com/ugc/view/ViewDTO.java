package com.ugc.view;

import java.util.Collection;

public class ViewDTO
{
    String _viewType = "";
    Collection<? extends Object> _collections;
    Object _transferObject;

    public Object getTransferObject() {
        return _transferObject;
    }

    public void setTransferObject(Object transferObject) {
        _transferObject = transferObject;
    }

    public Collection<? extends Object> getCollections() {
        return _collections;
    }

    public void setCollections(Collection<? extends Object> collections) {
        _collections = collections;
    }

    public String getViewType() {
        return _viewType;
    }

    public void setViewType(String viewType) {
        _viewType = viewType;
    }
}


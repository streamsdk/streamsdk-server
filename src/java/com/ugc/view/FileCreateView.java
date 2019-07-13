package com.ugc.view;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class FileCreateView extends AbstractView
{
    protected void renderMergedOutputModel(Map map, HttpServletRequest req,
                                           HttpServletResponse res) throws Exception
    {
        /*ViewDTO view = (ViewDTO)map.get(ViewConstants.VIEW_DTO);
        MediaUpload mediaUpload = (MediaUpload) view.getTransferObject();
        Element root = new org.jdom.Element("file");
        XmlUtils.addElement("id", mediaUpload.getFileId(), root);
        Document document = XmlUtils.createDocument(root);
        XMLOutputter outputter = new XMLOutputter();
        ServletOutputStream outputStream = res.getOutputStream();
        outputter.output(document, outputStream);
        outputStream.close();*/
    }
}
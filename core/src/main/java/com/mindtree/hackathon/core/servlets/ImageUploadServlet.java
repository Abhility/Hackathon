package com.mindtree.hackathon.core.servlets;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory ;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static org.apache.commons.fileupload.servlet.ServletFileUpload.isMultipartContent;


@Component(service=Servlet.class,
        property={
                Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths="+ "/bin/updamimages"
           })
public class ImageUploadServlet extends SlingAllMethodsServlet {
     
	private static final long serialVersionUID = 1L;


     //Inject a Sling ResourceResolverFactory
 @Reference
 private ResourceResolverFactory resolverFactory;
      
     @Override
     protected void doPost(final SlingHttpServletRequest req,
             final SlingHttpServletResponse resp) throws ServletException, IOException {
     try
     {
     final boolean isMultipart = isMultipartContent(req);
     PrintWriter out = null;
        
       out = resp.getWriter();
       if (isMultipart) {
         final java.util.Map<String, org.apache.sling.api.request.RequestParameter[]> params = req.getRequestParameterMap();
         for (final java.util.Map.Entry<String, org.apache.sling.api.request.RequestParameter[]> pairs : params.entrySet()) {
           final String k = pairs.getKey();
           final org.apache.sling.api.request.RequestParameter[] pArr = pairs.getValue();
           final org.apache.sling.api.request.RequestParameter param = pArr[0];
           final InputStream stream = param.getInputStream();
             
               //Save the uploaded file into the Adobe CQ DAM
            out.println( writeToDam(stream,param.getFileName()));
            
         }
       }
     }
        
     catch (Exception e) {
         e.printStackTrace();
     }
    
 }
    
  
//Save the uploaded file into the AEM DAM using AssetManager APIs
private String writeToDam(InputStream is, String fileName)
{
    Map<String, Object> param = new HashMap<String, Object>();
   param.put(ResourceResolverFactory.SUBSERVICE, "datawrite");
    //param.put("admin", "admin");
    ResourceResolver resolver = null;
 
        
    try {
                   
        //Invoke the adaptTo method to create a Session used to create a QueryManager
        resolver = resolverFactory.getServiceResourceResolver(param);
        
        //Use AssetManager to place the file into the AEM DAM
        com.day.cq.dam.api.AssetManager assetMgr = resolver.adaptTo(com.day.cq.dam.api.AssetManager.class);
        String newFile = "/content/dam/hackathon/receipts/"+fileName ; 
  
        assetMgr.createAsset(newFile, is,"application/pdf", true);
      
 
        // Return the path to the file was stored
        return newFile;
}
catch(Exception e)
{
e.printStackTrace();
}
return null;
}
 
}
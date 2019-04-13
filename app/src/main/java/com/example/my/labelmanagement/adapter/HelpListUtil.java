package com.example.my.labelmanagement.adapter;


public class HelpListUtil {
    private String helpTitle;
    private String helpContent;

    public HelpListUtil(String helpTitle){
        this.helpTitle = helpTitle;
    }

    public HelpListUtil(String helpTitle, String helpContent){
        this.helpTitle = helpTitle;
        this.helpContent = helpContent;
    }

    public void setHelpTitle(String helpTitle) {
        this.helpTitle = helpTitle;
    }

    public void setHelpContent(String helpContent){
        this.helpContent = helpContent;
    }

    public String getHelpTitle(){
        return helpTitle;
    }

    public String getHelpContent(){
        return helpContent;
    }
}

package ru.infocom.university.data;

public class Teacher {

    private String mName;
    private String mPost;

    public Teacher(String name, String post) {
        mName = name;
        mPost = post;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPost() {
        return mPost;
    }

    public void setPost(String post) {
        mPost = post;
    }
}

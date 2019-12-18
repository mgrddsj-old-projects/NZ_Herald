package com.jesse.nzherald;

public class Article
{
    String title;
    String author;
    String date;
    String description;
    String photoURL;
    String newsURL;

    public Article(String _title, String _author, String _date, String _description, String _photoURL, String _newsURL)
    {
        title = _title;
        author = _author;
        date = _date;
        description = _description;
        photoURL = _photoURL;
        newsURL = _newsURL;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPhotoURL()
    {
        return photoURL;
    }

    public void setPhotoURL(String photoURL)
    {
        this.photoURL = photoURL;
    }
}

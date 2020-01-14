package kr.chirokyel.lifeTool;

public class _9_Book_1_Form {

    public _9_Book_1_Form(String title, String author, String publisher, String image, String description, String link) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.image = image;
        this.description = description;
        this.link = link;
    };


    String title;
    String author;
    String publisher;
    String image;
    String description;

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    String link;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}

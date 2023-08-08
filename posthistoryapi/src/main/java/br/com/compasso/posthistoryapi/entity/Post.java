//package br.com.compasso.posthistoryapi.entity;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//public class Post {
//
//  @Id
//  private Long id;
//  private String title;
//  private String body;
////  private PostState state;
////  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
////  private List<History> histories;
//
////  public void addHistory(History history){
////    this.histories.add(history);
////  }
////  public void changeState(PostState state){
////    this.state = state;
////  }
////  public void process() {
////    state.process(this);
////  }
//}

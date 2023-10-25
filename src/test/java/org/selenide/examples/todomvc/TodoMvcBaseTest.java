package org.selenide.examples.todomvc;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.checked;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.clearBrowserCookies;
import static com.codeborne.selenide.Selenide.clearBrowserLocalStorage;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.hasWebDriverStarted;

abstract class TodoMvcBaseTest {
  private final String url;
  protected TodoMvcBaseTest(String url) {
    this.url = url;
  }

  @BeforeEach
  public void setUp() {
    Configuration.fastSetValue = false;
    if (hasWebDriverStarted()) {
      clearBrowserCookies();
      clearBrowserLocalStorage();
    }
    open(url);
  }

  @Test
  public void canAddTodoItem() {
    $(".new-todo").setValue("make coffee").pressEnter();
    $(".new-todo").setValue("drink coffee").pressEnter();

    $$(".todo-list .view").shouldHave(size(2));
    $(".todo-count").shouldHave(text("2 items left"));
    $(".filters a", 0).shouldHave(text("All"), cssClass("selected"));
    $(".filters a", 1).shouldHave(text("Active")).shouldNotHave(cssClass("selected"));
    $(".filters a", 2).shouldHave(text("Completed")).shouldNotHave(cssClass("selected"));
  }

  @Test
  public void canMarkItemAsCompleted() {
    addItem("Run");
    addItem("Walk");
    addItem("Swim");
    addItem("Rest");

    $$(".todo-list .view").shouldHave(size(4));
    $(".todo-count").shouldHave(text("4 items left"));
    $(".todo-list li", 1).shouldNotHave(cssClass("completed"));

    $(".todo-list li", 1).find(".toggle").click();
    $(".todo-list li", 1).find(".toggle").shouldBe(checked);
    $(".todo-list li", 1).find("label").shouldHave(text("Walk"));

    $(".todo-list li", 1).shouldHave(cssClass("completed"));
    $(".todo-count").shouldHave(text("3 items left"));
  }

  @Test
  public void canFilterActiveTodoItems() {
    refresh();
    sleep(1000);
    addItem("One");
    addItem("Two");
    addItem("Three");

    $$(".todo-list").shouldHave(size(1));
    $$(".todo-list .view").shouldHave(size(3));
    $(".todo-count").shouldHave(text("3 items left"));

    $(".filters a", 1).shouldHave(text("Active")).click();
    $$(".todo-list .view").shouldHave(size(3));
    $(".todo-count").shouldHave(text("3 items left"));
    $(".filters a", 1).shouldHave(text("Active"), cssClass("selected"));
    $(".filters a", 0).shouldHave(text("All")).shouldNotHave(cssClass("selected"));
  }

  @Test
  public void canFilterCompletedTodoItems() {
    addItem("One");
    addItem("Two");
    addItem("Three");

    $$(".todo-list .view").shouldHave(size(3));
    $(".todo-count").shouldHave(text("3 items left"));

    $(".filters a", 2).shouldHave(text("Completed")).click();
    $$(".todo-list .view").shouldHave(size(0));
    $(".todo-count").shouldHave(text("3 items left"));
    $(".filters a", 2).shouldHave(text("Completed"), cssClass("selected"));
    $(".filters a", 0).shouldHave(text("All")).shouldNotHave(cssClass("selected"));
  }

  @Test
  public void canDestroyTodoItem() {
    addItem("One");
    addItem("Two");
    addItem("Three");

    $$(".todo-list .view").shouldHave(size(3));
    $(".todo-count").shouldHave(text("3 items left"));

    $(".todo-list li", 2)
        .hover()
        .find(".destroy").click();

    $$(".todo-list .view").shouldHave(size(2));
    $(".todo-count").shouldHave(text("2 items left"));
  }

  @Test
  public void canAddHundredOfItems() {
    for (int i = 0; i < 100; i++) {
      addItem("sausage #" + i);
    }
    $(".todo-count").shouldHave(text("100 items left"));
  }

  @Test
  public void canRemoveAllItems_slow() {
    addItems(20);

    for (int i = 20; i > 0; i--) {
      $(".todo-count").should(matchText(i + " items? left"));
      $(".todo-list li", i - 1)
          .scrollTo()
          .hover()
          .find(".destroy").click();
    }

    $(".todo-count").should(disappear);
  }

  @Test
  public void canRemoveAllItems_fast() {
    addItems(20);

    for (int i = 0; i < 20; i++) {
      $(".todo-count").should(matchText((20 - i) + " items? left"));
      $(".todo-list li")
          .hover()
          .find(".destroy").click();
    }

    $(".todo-count").should(disappear);
  }

  private void addItem(String name) {
    $(".new-todo").setValue(name).pressEnter();
  }

  private void addItems(int count) {
    Configuration.fastSetValue = true;
    for (int i = 0; i < count; i++) {
      addItem("sausage #" + i);
    }
    $(".todo-count").shouldHave(text(count + " items left"));
  }
}

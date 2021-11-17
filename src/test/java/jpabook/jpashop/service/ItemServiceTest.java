package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Album;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Item.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ItemServiceTest {
    @Autowired
    ItemService itemService;

    @Test
    public void 상품저장() {
        Movie movie = new Movie();
        setItem(movie, "해리포터", 10000, 1000);
        movie.setActor("마법사들");
        movie.setDirector("(주)마법사들");
        Long itemId = itemService.saveItem(movie);

        Book book = new Book();
        setItem(book, "동물농장", 12000, 100);
        book.setAuthor("조지 오웰");
        book.setIsbn("1ASKJ2-QEWK2");
        itemService.saveItem(book);

        Album album = new Album();
        setItem(album, "Modern 사진접", 7000, 1500);
        album.setArtist("You");
        album.setEtc("모던한 형태로 만들어진 사진이 들어가지 않은 사진첩입니다.");
        itemService.saveItem(album);

        assertEquals(movie, itemService.findOne(itemId));
        assertEquals(3, itemService.findItems().size());
    }

    private void setItem(Item item, String name, int price, int stock) {
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stock);
    }
}
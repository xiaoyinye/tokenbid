package com.tokenbid.services;


import com.tokenbid.models.Item;
import com.tokenbid.repositories.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.mockito.Mockito.verify;

class ItemServiceTest {
    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    private ItemService itemService;

    @BeforeEach
    public void initBefore() {
        itemService = new ItemService(itemRepository);
    }

    private Item createItem() {
        Item item = new Item();
        item.setItemId(1);
        item.setTitle("deer pic");
        return item;
    }

    @Test
    public void shouldAddItem() {
        Item item = createItem();
        Mockito.when(itemRepository.save(item)).thenReturn(item);
        int actual = itemService.add(item);
        Assertions.assertEquals(1, actual);
    }

    @Test
    public void shouldUpdateItem() {
        Item item = createItem();
        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        itemService.update(item);
        verify(itemRepository).save(item);
    }

    @Test
    public void shouldDeleteItem() {
        Item item = createItem();
        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        itemService.delete(1);
        verify(itemRepository).deleteById(1);
    }

    @Test
    public void shouldGetItemById() {
        Item item = createItem();
        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Item actual = itemService.getById(1);
        Assertions.assertEquals(1, actual.getItemId());
        Assertions.assertEquals("deer pic", actual.getTitle());
    }

    @Test
    public void shouldGetItemByIdNull() {
        Optional<Item> item = Optional.empty();
        Mockito.when(itemRepository.findById(1)).thenReturn(item);
        Item actual = itemService.getById(1);
        Assertions.assertEquals(null, actual);
    }

    @Test
    public void shouldGetAll() {
        itemService.getAll();
        verify(itemRepository).findAll();
    }
}
package repository.book;

import java.util.*;

public class Cache<T> { //vrem sa o folosim atat pentru carti, cat si pentru user..... => generic classes

    public List<T> storage;

    public List<T> load(){
        return storage;
    }

    public void save(List<T> storage){ //cache-ul va fi incarcat tot odata!!!
        this.storage = storage;
    }

    public boolean hasResult(){
        return storage != null;
    }

    public void invalidateCache(){
        storage = null; //pentur cand se introduce ceva in baza de date
    }

}

package com.otus.avalieva.library.dao;

import com.otus.avalieva.library.domain.Genre;

import java.util.List;

public interface GenreDao {
   List<Genre> allGenres();
   void addGenre(Genre genre);
   Genre findGenreById(long id);
   void deleteGenre(long id);
   Genre findGenreByName(String name);
}

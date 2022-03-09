package com.liverary.book.springboot.service;

import com.liverary.book.springboot.domain.book.Book;
import com.liverary.book.springboot.domain.book.BookRepository;
import com.liverary.book.springboot.web.dto.book.BookIntroDto;
import com.liverary.book.springboot.web.dto.book.BookResponseDto;
import com.liverary.book.springboot.web.dto.book.BookSaveRequestDto;
import com.liverary.book.springboot.web.dto.book.BookUpdateRequestDto;
import com.liverary.book.springboot.web.dto.book.BookUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    // 책 저장
    @Transactional
    public Long save (BookSaveRequestDto bookSaveRequestDto){
        return bookRepository.save(bookSaveRequestDto.toEntity()).getBookKey();
    }

    // id별로 책 찾기
    public BookResponseDto findById(Long id){
        Book entity = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다 id = " + id));
        return new BookResponseDto(entity);
    }

    public Book findByIdBook(Long id){
        Book entity = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다 id = " + id));
        return entity;
    }

    // 등록일 기준 정렬 후 책 정보 출력
    @Transactional(readOnly = true)
    public List<BookIntroDto> findAllDesc(){
        return bookRepository.findAll(
                Sort.by(Sort.Direction.DESC,"createdDate"))
                .stream()
                .map(BookIntroDto::new)
                .collect(Collectors.toList());
    }

    // 검색어 기능
    @Transactional(readOnly = true)
    public List <BookIntroDto> findBySearch(String str ){
        return bookRepository.findBySearch(str).stream().map(BookIntroDto::new).collect(Collectors.toList());
    }
    @Transactional
    public Long update(Long id, BookUpdateRequestDto requestDto){
        Book book = bookRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        book.update(requestDto.getBookIntro(), requestDto.getBookContent());
        return id;
    }

    //

    @Transactional
    public void delete(Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 책이 없습니다. id=" + id));

        bookRepository.delete(book);
    }
    @Transactional
    public BookSaveRequestDto getBook(Long id){
        Book book = bookRepository.findById(id).get();
        BookSaveRequestDto bookSaveRequestDto = BookSaveRequestDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .bookIntro(book.getBookIntro())
                .bookContent(book.getBookContent())
                .country(book.getCountry())
                .totalPage(book.getTotalPage())
                .publishedDate(book.getPublishedDate())
                .publisher(book.getPublisher())
                .fileId(book.getFileId()).build();
        return bookSaveRequestDto;
    }

}

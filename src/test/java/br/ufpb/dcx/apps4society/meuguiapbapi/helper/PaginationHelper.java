package br.ufpb.dcx.apps4society.meuguiapbapi.helper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PaginationHelper {
    public static <T> Page<T> createPage(List<T> content, Pageable pageable, long total) {
        return new PageImpl<>(content, pageable, total);
    }

    public static <T> Page<T> createEmptyPage(Pageable pageable) {
        return new PageImpl<>(List.of(), pageable, 0);
    }

    public static <T> Page<T> createPageWithContent(List<T> content, Pageable pageable) {
        return new PageImpl<>(content, pageable, content.size());
    }

    public static Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size);
    }

    public static Pageable createDefaultPageable() {
        return createPageable(0, 20);
    }
}

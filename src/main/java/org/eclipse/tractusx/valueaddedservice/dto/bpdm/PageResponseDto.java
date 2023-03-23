package org.eclipse.tractusx.valueaddedservice.dto.bpdm;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class PageResponseDto {

    private Long totalElements;
    private Long totalPages;
    private Integer page;
    private Integer contentSize;
    private Collection<LegalEntityMatchResponse> content;
}

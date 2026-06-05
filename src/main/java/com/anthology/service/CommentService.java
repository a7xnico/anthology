package com.anthology.service;

import com.anthology.dto.requests.CommentsRequest;
import com.anthology.dto.responses.CommentsResponse;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.CommentsMapper;
import com.anthology.model.Comment;
import com.anthology.model.SongVersion;
import com.anthology.model.User;
import com.anthology.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final CommentsMapper mapper;
    private final UserService userService;
    private final SongVersionService songVersionService;


    public CommentsResponse createComment(CommentsRequest request)
    {
        User user=userService.findUserById(request.idUser());
        SongVersion songVersion=songVersionService.findSongVersionById(request.SongVersion());
        Comment comment=mapper.toEntity(request);

        return mapper.toDto(repository.save(comment));
    }
    public Comment findCommentById(Long id)
    {
        return repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("no se encontro el comentario"));
    }

    public CommentsResponse findByid(Long id)
    {
        return mapper.toDto(findCommentById(id));
    }
    public List<CommentsResponse>findAll()
    {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }
    public void deleateComment(Long id)
    {
        Comment comment=findCommentById(id);
        repository.delete(comment);
    }


}

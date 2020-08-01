package br.com.project.forum.controller;

import br.com.project.forum.controller.dto.DetalhesTopicoDTO;
import br.com.project.forum.controller.dto.TopicoDTO;
import br.com.project.forum.controller.dto.TopicoForm;
import br.com.project.forum.model.Topico;
import br.com.project.forum.repository.RepositoryCurso;
import br.com.project.forum.repository.RepositoryTopico;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@RestController//Ira mapear todos os retornos de metodos dentro do meu controller como responseBody
@RequiredArgsConstructor
@AllArgsConstructor
@RequestMapping(value = "/api/v1/topicos")
public class ControllerTopico {

    private RepositoryTopico repositoryTopico;

    private RepositoryCurso repositoryCurso;


    @GetMapping()
    @Cacheable(value = "listaDeTopicos") //ativando cache
    public Page<TopicoDTO> lista(@RequestParam(required = false)String cursoNome,
                                 @PageableDefault(
                                         sort = "id",
                                         direction = Sort.Direction.DESC,
                                         page = 0,
                                         size = 10) Pageable page) {

        Page<Topico> topicos;

        if (Objects.isNull(cursoNome)) {
            topicos = repositoryTopico.findAll(page);
        } else {
            topicos = repositoryTopico.findByCursoNome(cursoNome, page);
        }
        return TopicoDTO.convert(topicos);
    }

    @PostMapping()
    @CacheEvict(value = "listaDeTopicos" , allEntries = true) //limpando cache
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form,
                                               UriComponentsBuilder uriBuilder){

        val topico = form.convert(repositoryCurso);

        repositoryTopico.save(topico);

        val uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DetalhesTopicoDTO> detalhar(@PathVariable("id") Long id) {

        Optional<Topico> response = repositoryTopico.findById(id);

        return response.map(topico -> ResponseEntity.ok()
                .body(new DetalhesTopicoDTO(topico)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping(value = "/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos" , allEntries = true) //limpando cache
    public ResponseEntity<TopicoDTO> atualizar(@PathVariable("id") Long id,
                                               @RequestBody @Valid TopicoForm topicoForm){
        Optional<Topico> optional = repositoryTopico.findById(id);

        if (optional.isPresent()) {
            Topico topico = topicoForm.atualizar(id, repositoryTopico);
            return ResponseEntity.ok().body(new TopicoDTO(topico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{id}")
    @CacheEvict(value = "listaDeTopicos" , allEntries = true) //limpando cache
    public ResponseEntity<?> remover(@PathVariable("id") Long id){

        Optional<Topico> optional = repositoryTopico.findById(id);

        if (optional.isPresent()) {
            repositoryTopico.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

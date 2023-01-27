package com.apiclient.clientapi.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apiclient.clientapi.dto.ClientDTO;
import com.apiclient.clientapi.entities.Client;
import com.apiclient.clientapi.repository.ClientRepository;
import com.apiclient.clientapi.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;
/*
	@Transactional(readOnly = true)
	public List<ClientDTO> findAll() {
		List<Client> list = repository.findAll();

		return list.stream().map(x -> new ClientDTO(x)).collect(Collectors.toList());
	}
*/
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Não tente novamente sem corrigir o problema. Você precisa fornecer um valor válido para o parâmetro especificado na resposta de erro."));
		return new ClientDTO(entity);

	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
			
			Client entity = new Client();
			entity.setName(dto.getName());
			entity.setCpf(dto.getCpf());
			entity.setBirthDate(dto.getBirthDate());
			entity.setChildren(dto.getChildren());
			entity.setIncome(dto.getIncome());
			entity =  repository.save(entity);
			return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity.setCpf(dto.getCpf());
			entity.setBirthDate(dto.getBirthDate());
			entity.setChildren(dto.getChildren());
			entity.setIncome(dto.getIncome());
			entity =  repository.save(entity);
			return new ClientDTO(entity);
			
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Não tente novamente sem corrigir o problema. Você precisa fornecer um valor válido para o parâmetro especificado na resposta de erro.");
		}
		
	}

	public void delete(Long id) {
		try {
			
			repository.deleteById(id);
		} 
		catch (EmptyResultDataAccessException e){
			throw new ResourceNotFoundException("Não tente novamente sem corrigir o problema. id provavelmente deletado.");
		}
	}

}

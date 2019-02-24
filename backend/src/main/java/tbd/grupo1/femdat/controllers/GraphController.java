package tbd.grupo1.femdat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.configurationprocessor.json.JSONStringer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import tbd.grupo1.femdat.model.Graph;
import tbd.grupo1.femdat.model.TwitterUser;
import tbd.grupo1.femdat.repositories.GraphRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@ComponentScan(basePackages="tbd.grupo1.femdat")
@RestController
@EnableAutoConfiguration
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    private GraphRepository graphRepository;

	@GetMapping("")
	public Map<String, Object> getGraph(){
		Collection<TwitterUser> result = graphRepository.graph();
    	return Graph.toD3Format(result);
    }

    @GetMapping("/{hashtag}")
    public Map<String, Object> getGraph(@PathVariable String hashtag){
        Collection<TwitterUser> result = graphRepository.graph();
        return Graph.toD3Format(result);
    }
}

package tbd.grupo1.femdat.controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;

import java.util.Map;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@ComponentScan(basePackages="tbd.grupo1.femdat")
@RestController
@EnableAutoConfiguration
@RequestMapping("/keywords")
public class KeyWordsController {

    @Autowired
    public ResourceLoader resourceLoader;

	@RequestMapping(value="", method = RequestMethod.GET)
	public List<String> getKeywords(){
		return KeyWord.findAll(resourceLoader);
	}

	@RequestMapping(value="/{word}", method = RequestMethod.POST, produces = "application/json")
    public Map<String, String> createKeyWord(@PathVariable String word) {
        return Collections.singletonMap("response", String.valueOf(KeyWord.save(resourceLoader, word)));
    }

    @RequestMapping(value = "/{word}", method = RequestMethod.DELETE, produces = "application/json")
    public Map<String, String> deleteKeyWord(@PathVariable String word) {
        return Collections.singletonMap("response", String.valueOf(KeyWord.delete(resourceLoader, word)));
    }
}

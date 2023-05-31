package bot.databases;

import bot.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index(){
        return jdbcTemplate.query("SELECT * FROM PERSON", new BeanPropertyRowMapper<>(Person.class));
    }


    public void save(Person person){
        jdbcTemplate.update("INSERT into Person(chatId,city) values(?,?)", person.getChatId(),person.getCity());
    }

    public void update(Person person,long id){
        jdbcTemplate.update("UPDATE Person set city=? where chatid = ?", person.getCity(),id);
    }

    public Person show(long ChatId){
        return jdbcTemplate.query("SELECT * FROM PERSON WHERE chatId = ?", new Object[]{ChatId},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }
}

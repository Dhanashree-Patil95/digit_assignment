package digit.academy.tutorial.repository;

import digit.academy.tutorial.repository.querybuilder.AdvocateClerkQueryBuilder;
import digit.academy.tutorial.repository.querybuilder.AdvocateQueryBuilder;
import digit.academy.tutorial.repository.rowmapper.AdvocateClerkMapper;
import digit.academy.tutorial.repository.rowmapper.NewAdvocateMapper;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateClerk;
import digit.academy.tutorial.web.models.AdvocateClerkSearchCriteria;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Slf4j
@Repository
public class AdvocateClerkRepository {


    @Autowired
    private AdvocateClerkQueryBuilder queryBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;



    /**
     * Fetches a list of Clerk applications based on the given search criteria.
     * It iterates through each search criterion and builds the query with corresponding parameters.
     * Executes the query using JdbcTemplate and maps the result to AdvocateClerk objects.
     *
     * @param searchCriteria A list of AdvocateClerkSearchCriteria objects that define the search filters.
     * @return A list of AdvocateClerk objects that match the given search criteria.
     */
    public List<AdvocateClerk> getClerkApplications(List<AdvocateClerkSearchCriteria> searchCriteria) {
        // Iterating through each search criterion to fetch results
        List<AdvocateClerk> list = new ArrayList<>();
        for (AdvocateClerkSearchCriteria search : searchCriteria) {
            Map<Integer, Object> params = new HashMap<>();
            // Building the query and getting parameters from the search criteria
            String query = queryBuilder.getClerkQueryAndParams(search, params);
            // PreparedStatementSetter is used to set the query parameters in the prepared statement
            var pss =  new PreparedStatementSetter() {
                public void setValues(@NotNull PreparedStatement preparedStatement) {
                    // Looping through each parameter and setting it in the prepared statement
                    params.forEach((i, s) -> {
                        try {
                            preparedStatement.setObject(i, s);
                        } catch (SQLException e) {
                            throw new RuntimeException(e); // Exception handling for SQL errors
                        }
                    });
                }
            };
            // Executing the query using JdbcTemplate, passing the PreparedStatementSetter and row mapper
            List<AdvocateClerk> advocateClerks = jdbcTemplate.query(query, pss, new AdvocateClerkMapper());
            // Adding the result to the final list
            list.addAll(advocateClerks);
        }
        // Returning the list of matching AdvocateClerk objects
        return list;
    }

}

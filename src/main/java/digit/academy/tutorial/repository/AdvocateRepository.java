package digit.academy.tutorial.repository;

import digit.academy.tutorial.repository.querybuilder.AdvocateQueryBuilder;
import digit.academy.tutorial.repository.rowmapper.AdvocateRowMapper;
import digit.academy.tutorial.repository.rowmapper.NewAdvocateMapper;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import lombok.extern.slf4j.Slf4j;
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
public class AdvocateRepository {

    @Autowired
    private AdvocateQueryBuilder queryBuilder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves a list of Advocate applications based on the provided search criteria.
     *
     * @param searchCriteria a list of AdvocateSearchCriteria objects that specify the search conditions.
     * @return a list of Advocate objects matching the search criteria.
     * @throws RuntimeException if any exception occurs during query execution.
     *
     * The method iterates over the provided list of search criteria, builds a query and parameters for each criterion,
     * and executes the query using JdbcTemplate. The results are mapped to Advocate objects using the NewAdvocateMapper
     * and aggregated into a single list to be returned.
     */

    public List<Advocate> getApplications(List<AdvocateSearchCriteria> searchCriteria) {
        try {
            List<Advocate> list = new ArrayList<>();
            for (AdvocateSearchCriteria search : searchCriteria) {
                // Prepare the query and parameters
                Map<Integer, Object> params = new HashMap<>();
                String query = queryBuilder.getQueryAndParams(search, params);
                // Set parameter values for the prepared statement
                var pss = new PreparedStatementSetter() {
                    public void setValues(@NotNull PreparedStatement preparedStatement) {
                        params.forEach((i, s) -> {
                            try {
                                preparedStatement.setObject(i, s);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                };
                // Execute the query and map the results to Advocate objects
                List<Advocate> advocates = jdbcTemplate.query(query, pss, new NewAdvocateMapper());
                list.addAll(advocates);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}


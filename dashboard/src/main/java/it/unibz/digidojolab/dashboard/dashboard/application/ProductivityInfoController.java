package it.unibz.digidojolab.dashboard.dashboard.application;

import it.unibz.digidojolab.dashboard.dashboard.domain.ManageProductivityInfo;
import it.unibz.digidojolab.dashboard.dashboard.domain.ProductivityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productivityinfo")
public class ProductivityInfoController {
    @Autowired
    private ManageProductivityInfo mpi;

    @PostMapping
    public ProductivityInfo create(@RequestBody CreateProductivityInfoDTO dto) {
        return mpi.insertPI(dto.getStartup_id(), dto.getTeamMember_id(), dto.getActivity_type());
    }
}

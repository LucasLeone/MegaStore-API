package grupo11.megastore.users.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import grupo11.megastore.users.dto.address.AddressDTO;
import grupo11.megastore.users.dto.address.CreateAddressDTO;
import grupo11.megastore.users.model.Address;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IAddressMapper {
    AddressDTO addressToAddressDTO(Address address);
    Address createAddressDTOToAddress(CreateAddressDTO createAddressDTO);
}

Endpoint Path Configuration
In this project, we utilize configuration properties to manage endpoint paths for various controllers. Below are the parameters used for configuring endpoint paths:

User Controller Paths

user.controller.path: The base path for all endpoints related to user operations.
user.register.path: Path for user registration endpoint. (/api/v1/user/register)
user.login.path: Path for user login endpoint. (/api/v1/user/login)
user.delete.path: Path for deleting a user. It expects the user ID as a path variable. (/api/v1/user/delete/{id})
user.edit.path: Path for editing user details. It expects the user ID as a path variable and accepts optional parameters for email, username, and password. (/api/v1/user/edit/{id})
user.verification.path: Path for verifying user details using email and token. It expects the email and token as path variables. (/api/v1/user/verification/{email}/{token})


Plane Controller Paths

plane.controller.path: The base path for all endpoints related to plane operations.
plane.create.plane.path: Path for creating a new plane. (/api/v1/plane/create-new-plane)
plane.create.visa.path: Path for creating a new visa for a plane. (/api/v1/plane/create-new-visa)
plane.get.planes.path: Path for getting all planes. (/api/v1/plane/get-all-planes)
plane.get.visas.path: Path for getting all visas associated with a plane. (/api/v1/plane/get-all-visas)
plane.edit.plane.path: Path for editing plane details. It expects the plane ID as a path variable. (/api/v1/plane/edit-plane/{id})
plane.edit.visa.path: Path for editing visa details. It expects the visa ID as a path variable. (/api/v1/plane/edit-visa/{id})
plane.delete.plane.path: Path for deleting a plane. It expects the plane ID as a path variable. (/api/v1/plane/delete-plane/{id})
plane.delete.visa.path: Path for deleting a visa. It expects the visa ID as a path variable. (/api/v1/plane/delete-visa/{id})

Package Controller Paths

package.controller.path: The base path for all endpoints related to package operations.
package.create.package.path: Path for creating a new package. (/api/v1/package/create-new-package)
package.create.benefit.path: Path for creating a new benefit for a package. (/api/v1/package/create-new-benefit-for-package)
package.create.roadmap.path: Path for creating a new roadmap for a package. (/api/v1/package/create-new-roadmap-for-package)
package.create.todo.path: Path for creating a new todo for a package. (/api/v1/package/create-new-todo-for-package)
package.get.packages.path: Path for getting all packages. (/api/v1/package/get-all-packages)
package.get.packages.from.country.path: Path for getting all packages from a specific country. (/api/v1/package/get-all-packages/from-country)
package.get.benefits.path: Path for getting all benefits associated with packages. (/api/v1/package/get-all-benefits)
package.get.roadmaps.path: Path for getting all roadmaps associated with packages. (/api/v1/package/get-all-roadmaps)
package.get.todos.path: Path for getting all todos associated with packages. (/api/v1/package/get-all-todos)
package.delete.package.path: Path for deleting a package. It expects the package ID as a path variable. (/api/v1/package/delete-package/{id})
package.delete.benefit.path: Path for deleting a benefit. It expects the benefit ID as a path variable. (/api/v1/package/delete-benefit/{id})
package.delete.roadmap.path: Path for deleting a roadmap. It expects the roadmap ID as a path variable. (/api/v1/package/delete-roadmap/{id})
package.delete.todo.path: Path for deleting a todo. It expects the todo ID as a path variable. (/api/v1/package/delete-todo/{id})

Hotel Controller Paths

hotel.controller.path: The base path for all endpoints related to hotel operations.
hotel.create.hotel.path: Path for creating a new hotel. (/api/v1/hotel/create-new-hotel)
hotel.create.room.path: Path for creating a new room for a hotel. (/api/v1/hotel/create-new-room)
hotel.get.hotels.path: Path for getting all hotels. (/api/v1/hotel/get-all-hotels)
hotel.get.rooms.path: Path for getting all rooms associated with a hotel. (/api/v1/hotel/get-all-rooms)
hotel.edit.hotel.path: Path for editing hotel details. It expects the hotel ID as a path variable. (/api/v1/hotel/edit-hotel/{id})
hotel.edit.room.path: Path for editing room details. It expects the room ID as a path variable. (/api/v1/hotel/edit-room/{id})
hotel.delete.hotel.path: Path for deleting a hotel. It expects the hotel ID as a path variable. (/api/v1/hotel/delete-hotel/{id})
hotel.delete.room.path: Path for deleting a room. It expects the room ID as a path variable. (/api/v1/hotel/delete-room/{id})

Country Controller Paths

country.controller.path: The base path for all endpoints related to country operations.
country.create.path: Path for creating a new country. (/api/v1/country/create-new-country)
country.get.all.path: Path for getting all countries. (/api/v1/country/get-all-countries)
country.edit.path: Path for editing country details. It expects the country ID as a path variable. (/api/v1/country/edit-country/{id})
country.delete.path: Path for deleting a country. It expects the country ID as a path variable. (/api/v1/country/delete-country/{id})

class User {
  User(this.name, this.email, this.phone, this.password, this.accessToken);

  User.empty()
      : name = '',
        email = '',
        phone = null,
        password = '',
        accessToken = '';

  String name;
  String email;
  String? phone;
  String password;
  String accessToken;
}

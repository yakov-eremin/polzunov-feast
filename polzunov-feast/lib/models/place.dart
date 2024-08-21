class Place {
  final int id;
  final String name;
  final String address;

  Place({
    required this.id,
    required this.name,
    required this.address,
  });

  Place.fromJson(Map<String, dynamic> json)
      : id = json['id'] as int,
        name = json['name'] as String,
        address = json['address'] as String;

  Map<String, dynamic> toJson() => {
        'id': id,
        'name': name,
        'address': address,
      };
}

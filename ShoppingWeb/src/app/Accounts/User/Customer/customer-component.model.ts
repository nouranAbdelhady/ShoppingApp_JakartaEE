export interface Customer {
  id?: string;
  username?: string;
  password?: string;
  email?: string;
  fullname?: string;
  type?: string;
  is_logged_in?: boolean;
  orders?: any[]; // array of orders
  products?: any[];
}

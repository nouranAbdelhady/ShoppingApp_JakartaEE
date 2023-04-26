import {GeographicalRegion} from "./Customer/geographical-region.model";

export class User {
  id?: any;
  username?: string;
  password?: string;
  fullname?: string;
  email?: string;
  geographicalRegion?: GeographicalRegion
  type?: string;
  is_logged_in?: boolean;
}

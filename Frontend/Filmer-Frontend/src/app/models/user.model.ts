/**
 * Interfejs reprezentujący użytkownika w aplikacji.
 * Zawiera podstawowe dane o użytkowniku, takie jak ID, nick i e-mail.
 * @export
 * @interface User
 */
export interface User {
  /**
   * Unikalny identyfikator użytkownika.
   * @type {number}
   */
  id_user: number;

  /**
   * Nick (nazwa użytkownika).
   * @type {string}
   */
  nick: string;

  /**
   * Adres e-mail użytkownika.
   * @type {string}
   */
  email: string;
}

export interface ContactsPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
